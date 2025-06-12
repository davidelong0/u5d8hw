package epicode.u5d8hw.dto;

import lombok.Data;

@Data
public class BlogPostDTO {
    private int id;
    private String category;
    private String title;
    private String content;
    private double readingTime;
    private String cover;
    private int authorId;
}
