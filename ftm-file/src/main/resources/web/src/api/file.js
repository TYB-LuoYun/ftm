import axios from 'axios'
axios.defaults.baseURL = '/api';

//获得文件
let getFiles=(userId,parentId)=>{
  return axios.get("file/list.action?uid="+userId+"&parentId="+parentId);
}

//查询文件
let queryFiles=(userId,fidCid,key)=>{
  return axios.get("file/query.action?userId="+userId+"&fidCid="+fidCid+"&key="+key);
}

//删除文件
let deleteFile=(fid)=>{
  return axios.get("file/delete.action?fid="+fid);
}

//删除文件夹
let deleteDir=(fid,uid)=>{
  return axios.get("dir/delete.action?fid="+fid+"&userId="+uid);
}
//删除文件夹
let getSpace=()=>{
  return axios.get("file/space.action");
}
//保存


//设置分享链接
let getShare=(uid,fid)=>{
   return axios.get("file/share.action?uid="+uid+"&fid="+fid);
}


let getFileByAdress=(address)=>{
  return axios.get("/file/getShareFile.action?address="+address);
}

let getFileDetail=(fid,password)=>{
  return axios.get("/file/getShareDetailFile.action?password="+password+"&fid="+fid);
}

//根据分享链接查询文件
export {getFiles,deleteFile,deleteDir,queryFiles,getSpace,getShare,getFileByAdress,getFileDetail}

