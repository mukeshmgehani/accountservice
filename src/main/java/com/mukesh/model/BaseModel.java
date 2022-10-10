/**
 * 
 */
package com.mukesh.model;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import lombok.Data;

/**
 * @author Mukesh Gehani
 *
 */
@Data
@MappedSuperclass
public abstract class BaseModel {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
