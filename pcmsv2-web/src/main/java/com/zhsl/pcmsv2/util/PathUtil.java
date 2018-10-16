package com.zhsl.pcmsv2.util;

public class PathUtil {
    private static String separator = System.getProperty("file.separator");

    public static String getFileBasePath(Boolean temp){

        String osName = System.getProperty("os.name");
        String basePath = "";
        if (temp == true) {
            if (osName.toLowerCase().startsWith("windows")){
                basePath = "D:/pcmsFileTemp/";
            } else {
                basePath = "/home/pcmsFileTemp/";
            }
        } else {
            if (osName.toLowerCase().startsWith("windows")) {
                basePath = "D:/pcmsFile/";
            } else {
                basePath = "/home/pcmsFile/";
            }
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }
    // relative path  月报
    public static String getMonthlyReportImagePath(String projectName, String date){
        String imagePath = "upload/monthlyreport/"+ projectName + "/" + date + "/";
        return imagePath.replace("/", separator);
    }

    // relative path  基础信息
    public static String getBaseInfoImagePath(String projectName){
        String imagePath = "upload/baseinfo/"+ projectName + "/";
        return imagePath.replace("/", separator);
    }

    // relative path  项目前期
    public static String getPreProgressImagePath(String projectName){
        String imagePath = "upload/preprogress/"+ projectName + "/";
        return imagePath.replace("/", separator);
    }

    // relative path  合同备案
    public static String getContractImagePath(String projectName){
        String imagePath = "upload/contract/"+ projectName + "/";
        return imagePath.replace("/", separator);
    }

    // relative path  年度投融资计划
    public static String getAnnualInvestmentImagePath(String projectName, String year){
        String imagePath = "upload/annualinvestment/"+ projectName + "/" + year + "/";
        return imagePath.replace("/", separator);
    }
    // relative path  招标备案
    public static String getTenderImagePath(String projectName){
        String imagePath = "upload/tender/"+ projectName + "/";
        return imagePath.replace("/", separator);
    }
    public static void main(String[] args) {
        System.out.println(PathUtil.getFileBasePath(false));
    }

}
