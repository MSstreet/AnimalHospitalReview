const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  outputDir: "dist",  // 빌드 타겟 디렉토리
  
  // PWA 설정
  pwa: {
    name: 'PetHospital',
    shortName: 'PetHospital',
    themeColor: '#3498db',
    backgroundColor: '#f8f9fa',
    display: 'standalone',
    orientation: 'portrait-primary',
    scope: '/',
    startUrl: '/',
    icons: [
      {
        src: 'favicon.ico',
        sizes: '16x16 32x32 48x48',
        type: 'image/x-icon'
      },
      {
        src: 'favicon.ico',
        sizes: '192x192',
        type: 'image/png',
        purpose: 'any maskable'
      },
      {
        src: 'favicon.ico',
        sizes: '512x512',
        type: 'image/png',
        purpose: 'any maskable'
      }
    ],
    workboxPluginMode: 'GenerateSW',
    workboxOptions: {
      skipWaiting: true,
      clientsClaim: true,
      runtimeCaching: [
        {
          urlPattern: /^https:\/\/fonts\.googleapis\.com\/.*/i,
          handler: 'CacheFirst',
          options: {
            cacheName: 'google-fonts-cache',
            expiration: {
              maxEntries: 10,
              maxAgeSeconds: 60 * 60 * 24 * 365 // 1 year
            },
            cacheableResponse: {
              statuses: [0, 200]
            }
          }
        },
        {
          urlPattern: /^https:\/\/cdnjs\.cloudflare\.com\/.*/i,
          handler: 'CacheFirst',
          options: {
            cacheName: 'cdnjs-cache',
            expiration: {
              maxEntries: 10,
              maxAgeSeconds: 60 * 60 * 24 * 365 // 1 year
            },
            cacheableResponse: {
              statuses: [0, 200]
            }
          }
        }
      ]
    }
  },
  
  // 개발 서버 설정
  devServer: {
    proxy: {
      '/api': {
        // '/api' 로 들어오면 포트 8081(스프링 서버)로 보낸다
        target: 'http://localhost:8081',
        changeOrigin: true // cross origin 허용
      }
    },
    https: false,
    host: '0.0.0.0',
    port: 8080
  },
  
  // 빌드 최적화
  configureWebpack: {
    optimization: {
      splitChunks: {
        chunks: 'all',
        cacheGroups: {
          vendor: {
            test: /[\\/]node_modules[\\/]/,
            name: 'vendors',
            chunks: 'all'
          }
        }
      }
    },
    performance: {
      hints: false,
      maxEntrypointSize: 512000,
      maxAssetSize: 512000
    }
  },
  
  // CSS 최적화
  css: {
    extract: {
      ignoreOrder: true
    }
  },
  
  // 소스맵 설정 (프로덕션에서는 비활성화)
  productionSourceMap: false,
  
  // 청크 파일명 설정
  chainWebpack: config => {
    config.output
      .filename('[name].[contenthash:8].js')
      .chunkFilename('[name].[contenthash:8].js')
  }
})