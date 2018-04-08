package bean;

import java.io.Serializable;

/**
 * Created by Sheffy on 2018/4/7.
 */

public class BookBean implements Serializable{

    public String BookId;
    public String UserId;
    public int ClassmateCount;

    public int getClassmateCount() {
        return ClassmateCount;
    }

    public void setClassmateCount(int classmateCount) {
        ClassmateCount = classmateCount;
    }

    public String getBookId() {
        return BookId;
    }

    public void setBookId(String bookId) {
        BookId = bookId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    @Override
    public String toString() {
        return "BookBean[" +
                "BookId=" + BookId +
                ", UserId=" + UserId +
                ",ClassmateCount="+ClassmateCount+
                "]";
    }
}