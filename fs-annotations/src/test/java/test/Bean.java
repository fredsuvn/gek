package test;

import lombok.Data;
import xyz.fsgik.annotations.Nullable;

/**
 * @author sunqian
 */
@Data
public class Bean {
    private @Nullable String p1;
    private String p2 = "p2";
}