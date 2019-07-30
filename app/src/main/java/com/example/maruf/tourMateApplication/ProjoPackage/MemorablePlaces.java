package com.example.maruf.tourMateApplication.ProjoPackage;
public class MemorablePlaces {
    private String memorablePlacesId;
    private String downloadUrl;

    public MemorablePlaces(){

    };

    public MemorablePlaces(String memorablePlacesId, String downloadUrl) {
        this.memorablePlacesId = memorablePlacesId;
        this.downloadUrl = downloadUrl;
    }

    public String getMemorablePlacesId() {
        return memorablePlacesId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
