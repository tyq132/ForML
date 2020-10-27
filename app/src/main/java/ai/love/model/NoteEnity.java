package ai.love.model;

import org.greenrobot.greendao.annotation.*;

import java.util.Date;


/**
 * Created by James Tang on 2020/10/2
 */
@Entity
public class NoteEnity {
    @Id
    private Long Id;
    private String title;
    private String content;
    private Date time;
    private String lable;
    private String imgResUrl;
    @Generated(hash = 1958126189)
    public NoteEnity(Long Id, String title, String content, Date time, String lable,
            String imgResUrl) {
        this.Id = Id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.lable = lable;
        this.imgResUrl = imgResUrl;
    }
    @Generated(hash = 1928580739)
    public NoteEnity() {
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getLable() {
        return this.lable;
    }
    public void setLable(String lable) {
        this.lable = lable;
    }
    public String getImgResUrl() {
        return this.imgResUrl;
    }
    public void setImgResUrl(String imgResUrl) {
        this.imgResUrl = imgResUrl;
    }
}
