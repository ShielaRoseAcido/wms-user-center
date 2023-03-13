package com.sunlife.ph.workflowmanagementsystem.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee name is required")
    @Pattern(regexp = "^[^.\\s]+(\\.[^.\\s]+)+$", message = "Employee name must be separated by periods and contain no spaces")
    private String employeeName;
}
