package util;

public enum FileType {
    APK("504B0304140008000800"),
    DEX("6465780A30333500"),
    ZIP("504B0304"),
    // ELF 32-bit LSB  shared object
    ELF_LSB_SHARED_OBJECT("7F454C460101010000000000000000000300280001000000"),
    // ELF 32-bit LSB  executable, ARM
    ELF_LSB_EXECUTABLE("7F454C46010101000000000000000000020028000100000");

    private String value;

    FileType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}