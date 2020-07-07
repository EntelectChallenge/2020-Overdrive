package za.co.entelect.challenge.network.dto;

import com.google.gson.annotations.SerializedName;

public class ExceptionSendDto {
    @SerializedName("message")
    private String message;

    @SerializedName("entryId")
    private String entryId;

    public ExceptionSendDto() {
    }

    public ExceptionSendDto(String message, String entryId) {
        this.message = message;
        this.entryId = entryId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }
}
