package ru.pin36bik.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.pin36bik.dto.WindowUserDTO;

import java.util.List;

@FeignClient(name = "window-service")
public interface WindowServiceClient {
    @GetMapping("/api/windows/user/{id}")
    List<WindowUserDTO> getWindowsByUserId(@PathVariable Long id);
}
