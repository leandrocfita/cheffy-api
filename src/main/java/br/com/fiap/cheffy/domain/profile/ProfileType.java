package br.com.fiap.cheffy.domain.profile;

public enum ProfileType {

    CLIENT("CLIENT"),
    OWNER("OWNER");

    private final String value;

    ProfileType(String value) {
        this.value = value;
    }

    public String getType() {
        return value;
    }
}
