
### 1. 依赖导入

### 2. 快速上手 
```java
import com.sunchatglm.GLM;

public class Main {
    public static void main(String[] args) {
        String apikey = "YOUR_API_KEY";
        GLM glm = new GLM(apikey);
        String answer = glm.ask("glm-4", "你好");
        System.out.println(answer);
    }
}
```


### TODO List
- [ ] `Maven` 仓库导入

