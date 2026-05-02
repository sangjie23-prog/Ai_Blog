import request from '../request'

/**
 * 上传图片
 * @param {File} file - 图片文件
 * @returns {Promise} 返回图片URL
 */
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/admin/upload/image',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
