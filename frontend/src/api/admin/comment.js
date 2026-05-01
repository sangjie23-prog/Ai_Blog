import request from './request'

// 获取评论列表
export function getCommentList(pageNum = 1, pageSize = 10, status = null) {
  return request({
    url: '/admin/comments',
    method: 'get',
    params: { pageNum, pageSize, status }
  })
}

// 审核通过
export function approveComment(id) {
  return request({
    url: `/admin/comments/${id}/approve`,
    method: 'put'
  })
}

// 拒绝评论
export function rejectComment(id) {
  return request({
    url: `/admin/comments/${id}/reject`,
    method: 'put'
  })
}

// 删除评论
export function deleteComment(id) {
  return request({
    url: `/admin/comments/${id}`,
    method: 'delete'
  })
}
