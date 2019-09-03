package com.yt.moment.beans;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class ImageUploadBean implements Serializable {


    public String big;
    public String small;

    public String originPic;
    public String compressPic;
    public String breavyPic;


    public ImageUploadBean() {
    }

    public ImageUploadBean(String originPic) {
        this.originPic = originPic;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        ImageUploadBean p1 = (ImageUploadBean) obj;//将Object向下转型为ImageUploadBean
        return this.originPic.equals(p1.originPic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originPic);
    }
}
