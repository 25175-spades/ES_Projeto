package pt.uportalegre.estgd.ei.es.projeto.domain;

import jakarta.persistence.Column;

import java.util.Objects;

public class Recipient {

    @Column
    private String address;

    protected Recipient() {

    }

    public Recipient(String address) {
        if (address == null || address.isBlank())
            throw new IllegalArgumentException("Endereço de destinatário não pode estar vazio");

        this.address = address.trim();
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Recipient recipient)) {
            return false;
        }

        return Objects.equals(address, recipient.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address);
    }
}
