package com.x.vscam.upload;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class UploadResponseBean {

    /**
     * uid : 7
     * scale : 0.66666666666667
     * origin : 76afa645d4e50ee051836c31a5973265
     * aperture : 2.4
     * iso : 50
     * gps : 123.34893,41.6395
     * unix : 1484213867
     * exif :{"COMPUTED":{"html":"width=\"3264\" height=\"2176\"","Height":2176,"Width":3264,"IsColor":1,
     * "ByteOrderMotorola":1,"ApertureFNumber":"f\/2.4","UserComment":"Processed with VSCOcam with b5 preset",
     * "UserCommentEncoding":"ASCII","Copyright":"Copyright 2015. All rights reserved.","Thumbnail.FileType":2,
     * "Thumbnail.MimeType":"image\/jpeg"},"IFD0":{"ImageDescription":"Processed with VSCOcam with b5 preset",
     * "Make":"Apple","Model":"iPhone 5c","Orientation":1,"XResolution":"72\/1","YResolution":"72\/1",
     * "ResolutionUnit":2,"Software":"VSCOcam","DateTime":"2015:02:15 02:14:15","HostComputer":"iPhone 5c",
     * "YCbCrPositioning":1,"Copyright":"Copyright 2015. All rights reserved.","Exif_IFD_Pointer":328,
     * "GPS_IFD_Pointer":1186},"EXIF":{"ExposureTime":"1\/20","FNumber":"12\/5","ExposureProgram":2,
     * "ISOSpeedRatings":50,"ExifVersion":"0221","DateTimeOriginal":"2015:02:15 02:14:15",
     * "DateTimeDigitized":"2015:02:15 02:14:15","ComponentsConfiguration":"\u0001\u0002\u0003\u0000",
     * "ShutterSpeedValue":"3799\/879","ApertureValue":"4845\/1918","BrightnessValue":"3182\/859",
     * "ExposureBiasValue":"0\/1","MeteringMode":5,"Flash":16,"FocalLength":"103\/25","SubjectLocation":[1631,1223,
     * 1795,1077],"UserComment":"ASCII\u0000\u0000\u0000Processed with VSCOcam with b5 preset",
     * "SubSecTimeOriginal":"174","SubSecTimeDigitized":"174","FlashPixVersion":"0100","ColorSpace":1,
     * "ExifImageWidth":3264,"ExifImageLength":2448,"SensingMethod":2,"SceneType":"\u0001","ExposureMode":0,
     * "WhiteBalance":0,"FocalLengthIn35mmFilm":33,"SceneCaptureType":0,"UndefinedTag:0xA432":["103\/25","103\/25",
     * "12\/5","12\/5"],"UndefinedTag:0xA433":"Apple","UndefinedTag:0xA434":"iPhone 5c back camera 4.12mm f\/2.4"},
     * "GPS":{"GPSLatitudeRef":"N","GPSLatitude":["41\/1","38\/1","2220\/100"],"GPSLongitudeRef":"E",
     * "GPSLongitude":["123\/1","20\/1","5615\/100"],"GPSAltitudeRef":"\u0000","GPSAltitude":"33043\/838",
     * "GPSTimeStamp":["18\/1","14\/1","14\/1"],"GPSSpeedRef":"K","GPSSpeed":"0\/1","GPSImgDirectionRef":"T",
     * "GPSImgDirection":"35994\/145","GPSDestBearingRef":"T","GPSDestBearing":"25315\/371",
     * "GPSDateStamp":"2015:02:14"}}
     * pid : 760
     */

    private String uid;
    private double scale;
    private String origin;
    private double aperture;
    private int iso;
    private String gps;
    private int unix;
    private String exif;
    private int pid;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public double getAperture() {
        return aperture;
    }

    public void setAperture(double aperture) {
        this.aperture = aperture;
    }

    public int getIso() {
        return iso;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public int getUnix() {
        return unix;
    }

    public void setUnix(int unix) {
        this.unix = unix;
    }

    public String getExif() {
        return exif;
    }

    public void setExif(String exif) {
        this.exif = exif;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

}
