package org.example.consumer_kafka.application;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@Entity
@Table(name = "items")
public class Item 
{
    @Id
    @JdbcTypeCode(Types.VARCHAR)
    @NonNull
    private UUID id;

    @NonNull
    private String name;

    @NonNull
    private LocalDateTime createdAt;
}
