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
@Table(name = "WMSUser")
public class WMSUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee name is required")
    @Pattern(regexp = "^[^.\\s]+(\\.[^.\\s]+)+$", message = "Employee name must be separated by periods and contain no spaces")
    private String employeeName;

    @NotBlank(message = "Employment type is required")
    @Pattern(regexp = "^\\S(.*\\S)?$", message = "Employment type cannot be blank or just spaces")
    private String employmentType;

    @NotBlank(message = "Role is required")
    private String role;  // Make sure you also update your entity to include 'role'

    //SQL
    //table1

}
