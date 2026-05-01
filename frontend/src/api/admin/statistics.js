import request from './request'

// 获取系统统计数据
export function getSystemStatistics() {
  return request({
    url: '/admin/statistics/system',
    method: 'get'
  })
}

// 获取热门文章
export function getHotArticles(limit = 10) {
  return request({
    url: '/admin/statistics/hot',
    method: 'get',
    params: { limit }
  })
}
