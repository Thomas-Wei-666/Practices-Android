package com.example.photoalbumactivity;




public class PhotoData implements Comparable<String>{
    private String photoPaths;

    public PhotoData(String photoPaths) {
        this.photoPaths = photoPaths;
    }

    public String getPhotoPaths() {
        return photoPaths;
    }

    @Override
    public int compareTo(String o) {
        return -1;
    }
}
