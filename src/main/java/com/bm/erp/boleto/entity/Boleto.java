package com.bm.erp.boleto.entity;

import com.bm.erp.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "boleto")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Boleto {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private BoletoStatus status = BoletoStatus.OPEN;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    public void markAsPaid(){
        if(this.status == BoletoStatus.PAID){
            return;
        }
        if(this.status != BoletoStatus.OPEN){
            throw new IllegalStateException("Cannot pay a boleto the is "+this.status);
        }
        this.status = BoletoStatus.PAID;
    }

    public void cancel(){
        if(this.status == BoletoStatus.CANCELLED){
            return;
        }
        if(this.status != BoletoStatus.OPEN){
            throw new IllegalStateException("Cannot cancel a boleto the is "+this.status);
        }
        this.status = BoletoStatus.CANCELLED;
    }
}
