package org.cdtu.website.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

@Data
@Table(value = "atomic_problem")
public class AtomicProblem {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;
    private String question;
    private String opA;
    private String opB;
    private String opC;
    private String opD;
    private Integer answer;
    private Integer type;
    private Long setId;
    private Integer order;

}
