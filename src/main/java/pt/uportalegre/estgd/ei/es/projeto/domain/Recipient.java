package pt.uportalegre.estgd.ei.es.projeto.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
public class Recipient {

    @Column (nullable = false)
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
        // Verifica se é o próprio objeto
        if (this == object) {
            return true;
        }

        // Verifica se é Recipient
        if (!(object instanceof Recipient recipient)) {
            return false;
        }

        // Compara os endereços
        return Objects.equals(address, recipient.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address);
    }
}
