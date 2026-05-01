import request from '../request'

/**
 * 获取后台文章列表
 * @param {Object} params - {pageNum, pageSize, status, keyword}
 */
export function getAdminArticles(params) {
  return request({
    url: '/admin/articles',
    method: 'get',
    params
  })
}

/**
 * 获取文章详情
 * @param {number} id - 文章ID
 */
export function getAdminArticle(id) {
  return request({
    url: `/admin/articles/${id}`,
    method: 'get'
  })
}

/**
 * 创建文章
 * @param {Object} data - 文章数据
 */
export function createAdminArticle(data) {
  return request({
    url: '/admin/articles',
    method: 'post',
    data
  })
}

/**
 * 更新文章
 * @param {number} id - 文章ID
 * @param {Object} data - 文章数据
 */
export function updateAdminArticle(id, data) {
  return request({
    url: `/admin/articles/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除文章
 * @param {number} id - 文章ID
 */
export function deleteAdminArticle(id) {
  return request({
    url: `/admin/articles/${id}`,
    method: 'delete'
  })
}

/**
 * 发布文章
 * @param {number} id - 文章ID
 */
export function publishArticle(id) {
  return request({
    url: `/admin/articles/${id}/publish`,
    method: 'post'
  })
}

/**
 * 撤回文章
 * @param {number} id - 文章ID
 */
export function unpublishArticle(id) {
  return request({
    url: `/admin/articles/${id}/unpublish`,
    method: 'post'
  })
}
