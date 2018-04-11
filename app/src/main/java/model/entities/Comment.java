package model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by walid on 11/04/2018.
 */

public class Comment implements Parcelable{

    public static final String USERCOMENT = "userComment";
    public static final String USERCOMMENTED = "userCommented";
    public static final String COMMENT = "comment";
        public static final String USER = "user";

    private String userComment;
    private String userCommented;
    private String comment;
    private Usuario user;

    public Comment() {
    }

    public Comment(String userComment, String userCommented, String comment,Usuario user) {
        this.userComment = userComment;
        this.userCommented = userCommented;
        this.comment = comment;
        this.user = user;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getUserCommented() {
        return userCommented;
    }

    public void setUserCommented(String userCommented) {
        this.userCommented = userCommented;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userComment);
        dest.writeString(userCommented);
        dest.writeString(comment);
        dest.writeParcelable(user,flags);
    }


    public Comment(Parcel in) {
        userComment = in.readString();
        userCommented = in.readString();
        comment = in.readString();
        user = (Usuario) in.readParcelable(Usuario.class.getClassLoader());
    }

    public static final Parcelable.Creator<Comment> CREATOR
            = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
