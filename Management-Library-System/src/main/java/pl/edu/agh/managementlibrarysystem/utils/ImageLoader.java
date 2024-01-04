package pl.edu.agh.managementlibrarysystem.utils;

import javafx.scene.image.Image;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("singleton")
@Log4j2
public class ImageLoader {
    private final Map<String, Image> cache = new HashMap<>();

    public ImageLoader() {
        cache.put("default", new Image("/images/default.jpg"));
    }

    public Image getImage(String key) {
        if (key == null) return cache.get("default");


        if (!cache.containsKey(key)) {
            Image image = new Image(key);
            cache.put(key, image);

            log.info("Image put into cache");
            return image;
        }

        log.info("Image loaded from cache");

        return cache.get(key);
    }
}
