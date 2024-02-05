
### 1. 依赖导入

### 2. 快速上手

```java
import com.sunchatglm.model.GLM;

public class Main {
    public static void main(String[] args) {
        String apikey = "YOUR_API_KEY";
        GLM glm = new GLM(apikey);
        String answer = glm.ask("glm-4", "你好");
        System.out.println(answer);
    }
}
```
更多API调用方式，请[点击](src/test/java/org/example/GLMApiTest.java)查看
ChatGLM APIKey查找 [传送门](https://open.bigmodel.cn/usercenter/apikeys)

### TODO List
- [ ] `Maven` 仓库导入
- [x] 支持`SSE`流式输出
