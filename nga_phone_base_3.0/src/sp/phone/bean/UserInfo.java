package sp.phone.bean;

public class UserInfo {
    String uid;//":1234567,//uid 
    String username;//":"xxx",//用户名 
    //"credit":20,//无用 
    //"medal":54,//徽章id 逗号分隔 
    //"reputation":"46_100",//无用 
    //"groupid":-1,//用户组 如果是-1使用下一个用户组 
    //"memberid":39,//用户组 
    String avatar;//":"",//头像 和以前一样 可能是字符串也可能是object 
    String yz;//":1,//激活状态 1激活 0未激活 -1nuke -2往下账号禁用 
    //"site":"",//个人版名 
    //"honor":"",//头衔 
    //"regdate":1199856844,//注册日期 
    String mute_time;//":0,//禁言到期时间 
    //"postnum":2409,//发帖数 
    String rvrc;//":0,//威望 
    //"money":23741,//金钱 铜币数 
    //"thisvisit":1363859920,//最后一次访问 
    String signature;//":"",//签名 
    //"nickname":"",//无用 
    //"bit_data":20//用户状态bit 

}
