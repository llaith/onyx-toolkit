/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.fault;

/**
 *
 */
public class FaultSuppression {

    private final FaultLocation location;
    private final SuppressionToken token;

    public FaultSuppression(final FaultLocation location, final SuppressionToken token) {
        this.location = location;
        this.token = token;
    }

    public FaultLocation location() {
        return this.location;
    }

    public SuppressionToken token() {
        return this.token;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        final FaultSuppression that = (FaultSuppression) o;

        if (this.location != null ? !this.location.equals(that.location) : that.location != null) return false;
        if (this.token != null ? !this.token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = this.location != null ? this.location.hashCode() : 0;
        result = 31 * result + (this.token != null ? this.token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FaultSuppression{" +
                "location=" + this.location +
                ", token=" + this.token +
                '}';
    }

}
