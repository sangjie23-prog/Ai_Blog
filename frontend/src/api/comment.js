import request from './request'

// 获取文章评论列表
export function getArticleComments(articleId) {
  return request({
    url: '/comments',
    method: 'get',
    params: { articleId }
  })
}

// 提交评论
export function submitComment(comment) {
  return request({
    url: '/comments',
    method: 'post',
    data: comment
  })
}
