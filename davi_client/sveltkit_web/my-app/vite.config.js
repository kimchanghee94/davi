import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	base: './',
	plugins: [sveltekit()],
	assetsInclude: ['**/*.jpg', '**/*.png'],
	server: {
		proxy: {
			'/api': {
				target: 'https://api.datawrapper.de/v3/charts',
				changeOrigin: true,
				secure: false,
				rewrite: (path) => path.replace(/^\/api/, '')
			},
			'/vworld-api': {
				target: 'https://api.vworld.kr/req/data',		//설정파일이라 동적으로 변경하기가 너무 어렵다...
				changeOrigin: true,
				secure: false,
				rewrite: (path) => path.replace(/^\/vworld-api/, ''),
				configure: (proxy, _options) => {
					proxy.on("error", (err, _req, _res) => {
						console.log("proxy error", err);
					});
					proxy.on("proxyReq", (proxyReq, req, _res) => {
						console.log(
							"Sending Request:",
							req.method,
							req.url,
							req,
							// _res,
							" => TO THE TARGET =>  ",
							proxyReq.method,
							proxyReq.protocol,
							proxyReq.host,
							proxyReq.path,
							JSON.stringify(proxyReq.getHeaders()),
						);
					});
					proxy.on("proxyRes", (proxyRes, req, _res) => {
						console.log(
							"Received Response from the Target:",
							proxyRes.statusCode,
							req.url,
							JSON.stringify(proxyRes.headers),
						);
					});
				},
			},
		}
	}
});
