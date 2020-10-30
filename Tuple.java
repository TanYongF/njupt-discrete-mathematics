import java.util.Objects;

public class Tuple {
    private String key;
    private String value;

    public Tuple(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
    public String getKey(){
        return  this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
