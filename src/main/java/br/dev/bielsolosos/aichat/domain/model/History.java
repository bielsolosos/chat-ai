package br.dev.bielsolosos.aichat.domain.model;

import br.dev.bielsolosos.aichat.infrastructre.enums.ModelVendorEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "history")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "vendor", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModelVendorEnum modelVendor;

}
