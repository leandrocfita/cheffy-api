package br.com.fiap.cheffy.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {

    private static final int SCALE = 2;

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount must not be null");

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must be greater than or equal to zero");
        }

        this.amount = amount.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public BigDecimal value() {
        return amount;
    }

    public Money add(Money other) {

        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        BigDecimal result = this.amount.subtract(other.amount);

        if (result.signum() < 0) {
            throw new IllegalArgumentException("Resulting amount cannot be negative");
        }

        return new Money(result);
    }

    public Money multiply(int factor) {
        if (factor < 0) {
            throw new IllegalArgumentException("Factor must be positive");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)));
    }

    public boolean isGreaterThan(Money other) {

        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}