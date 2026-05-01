import request from './request'

// 获取文章列表（分页）
export function getArticleList(pageNum = 1, pageSize = 10, status = 1) {
  return request({
    url: '/articles',
    method: 'get',
    params: { pageNum, pageSize, status }
  })
}

// 获取文章详情
export function getArticleDetail(id) {
  return request({
    url: `/articles/${id}`,
    method: 'get'
  })
}

// 点赞文章
export function likeArticle(id) {
  return request({
    url: `/articles/${id}/like`,
    method: 'post'
  })
}
