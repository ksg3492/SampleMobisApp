package com.sunggil.samplemobisapp.data.model.melon

class MelonStreamingItem {
    var RESULT= ""
    var ACTIONCODE = "100"
    var MESSAGE= ""
    var VIEWTYPE= ""
    var staticDomain= ""
    var httpsDomain= ""

    var GETPATHINFO : PATHINFO? = null
    var CONTENTSINFO : Object? = null
    var ISFLACSTOK = false
    var ISFLAC16STALLOK = false

    inner class PATHINFO {
        //-1 : 전체재생, 나머지는 입력된 숫자만큼만 재생
        var PLAYTIME = ""
        var CID = ""
        var PATH = ""
        var LOGGINGTOKEN = ""
        var PROTOCOLTYPE = ""
        var ISHTTPS = false
        var C = ""
        var METATYPE = ""
        var BITRATE = ""
        var ISSTOWNPRODUCT = false
        var FILEUPDTDATE = ""
        var FILESIZE = ""
    }
}