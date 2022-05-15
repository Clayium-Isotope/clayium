package invtweaks.api.container;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ChestContainer {
  boolean showButtons() default true;
  
  int rowSize() default 9;
  
  boolean isLargeChest() default false;
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD})
  public static @interface IsLargeCallback {}
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.METHOD})
  public static @interface RowSizeCallback {}
}


