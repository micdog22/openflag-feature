
package com.micdog.openflag.web;

import com.micdog.openflag.model.Flag;
import com.micdog.openflag.repo.FlagRepository;
import com.micdog.openflag.service.EvaluationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sdk")
public class SdkController {

    private final FlagRepository repo;
    private final EvaluationService eval;

    public SdkController(FlagRepository repo, EvaluationService eval) {
        this.repo = repo;
        this.eval = eval;
    }

    @GetMapping(value="/evaluate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Object> evaluate(
            @RequestParam String flagKey,
            @RequestParam(required = false) String userId,
            @RequestParam Map<String,String> params
    ) {
        Flag flag = repo.findByFlagKey(flagKey).orElse(null);
        var res = eval.evaluate(flag, userId, params);
        Map<String,Object> out = new HashMap<>();
        out.put("flagKey", flagKey);
        out.put("enabled", res.enabled());
        out.put("reason", res.reason());
        return out;
    }
}
