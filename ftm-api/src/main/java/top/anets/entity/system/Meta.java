package top.anets.entity.system;

import lombok.Data;

@Data
public class Meta {
    private String title;
    private String icon;
    private Boolean breadcrumbHidden;
    private Boolean noClosable;
//    private Boolean dot;
}
