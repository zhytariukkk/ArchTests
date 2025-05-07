package lr1.lr1.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    private Long id;

    @NotBlank(message = "Ім'я пристрою не може бути порожнім")
    private String name;

    @NotBlank(message = "Тип пристрою не може бути порожнім")
    private String type;

    private boolean status;

    @NotNull(message = "Кімната не може бути порожньою")
    private Long roomId;
}
