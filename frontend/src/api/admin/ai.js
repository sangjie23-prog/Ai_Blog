import request from '../request'

export function generateSummary(content) {
  return request({
    url: '/admin/ai/summary',
    method: 'post',
    data: { content }
  })
}

export function generateTags(content) {
  return request({
    url: '/admin/ai/tags',
    method: 'post',
    data: { content }
  })
}

export function generateAll(content) {
  return request({
    url: '/admin/ai/generate',
    method: 'post',
    data: { content }
  })
}
