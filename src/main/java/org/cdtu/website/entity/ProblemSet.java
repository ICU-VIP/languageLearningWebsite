package org.cdtu.website.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Table(value = "problem_set")
public class ProblemSet {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;
    private String name;
    private String description;
    private String audio;
    private Integer exp;
    private Integer type;
    @RelationOneToMany(selfField = "id", targetTable = "atomic_problem", targetField = "set_id")
    private List<AtomicProblem> atomicProblems;
    @Column(onInsertValue = "0")
    private Integer order;
    private Long createBy;
    @Column(ignore = true)
    private String creatorName;
    private Long updateBy;
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;
    @Column(onUpdateValue = "now()")
    private LocalDateTime updateTime;

}
