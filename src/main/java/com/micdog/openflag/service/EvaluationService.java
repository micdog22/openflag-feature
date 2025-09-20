
package com.micdog.openflag.service;

import com.micdog.openflag.model.Flag;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EvaluationService {

    public record EvalResult(boolean enabled, String reason) {}

    public EvalResult evaluate(Flag flag, String userId, Map<String,String> attrs) {
        if (flag == null) return new EvalResult(false, "not_found");
        if (!flag.isEnabled()) return new EvalResult(false, "disabled");

        Set<String> includes = csvSet(flag.getIncludeIds());
        Set<String> excludes = csvSet(flag.getExcludeIds());

        if (userId != null && excludes.contains(userId)) return new EvalResult(false, "excluded");
        if (userId != null && includes.contains(userId)) return new EvalResult(true, "included");

        // attribute
        String key = flag.getAttributeKey();
        if (key != null && !key.isBlank()) {
            Set<String> allowed = csvSet(flag.getAttributeAllowedCsv());
            String val = attrs.get("attr."+key);
            if (val == null || !allowed.contains(val)) return new EvalResult(false, "attr_not_allowed");
        }

        int pct = Math.max(0, Math.min(100, flag.getPercentage()==null?0:flag.getPercentage()));
        if (pct >= 100) return new EvalResult(true, "percentage_100");
        if (pct <= 0) return new EvalResult(true, "enabled_no_percentage");

        // percentage rollout by hashing userId
        if (userId == null || userId.isBlank()) return new EvalResult(false, "no_user");
        int bucket = bucket(userId);
        return new EvalResult(bucket < pct, "bucket_"+bucket);
    }

    private Set<String> csvSet(String csv) {
        if (csv == null || csv.isBlank()) return Collections.emptySet();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s->!s.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private int bucket(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest(s.getBytes(StandardCharsets.UTF_8));
            // first 4 bytes as unsigned int
            long v = ((long)(d[0] & 0xFF) << 24) | ((long)(d[1] & 0xFF) << 16) | ((long)(d[2] & 0xFF) << 8) | (long)(d[3] & 0xFF);
            if (v < 0) v = -v;
            return (int)(v % 100);
        } catch (Exception e) {
            return 0;
        }
    }
}
