const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    landingPage: path.resolve(__dirname, 'src', 'pages', 'landingPage.js'),
    loginPage: path.resolve(__dirname, 'src', 'pages', 'loginPage.js'),
    drinkPage: path.resolve(__dirname, 'src', 'pages', 'drinkPage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: 'http://localhost:8080/index.html',
    // disableHostChecks, otherwise we get an error about headers and the page won't render
    allowedHosts: "all",
    static: 'packaging_additional_published_artifacts',
    // overlay shows a full-screen overlay in the browser when there are compiler errors or warnings
    client: {
      overlay: true
    },
    proxy: [
      {
        context: [
          '/drinks',
          '/users',
          '/login'
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/drink.html',
      filename: 'drink.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/login.html',
      filename: 'login.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ],
  module: {
    rules: [
      {
        test: /\.(scss)$/,
        use: [
          {
            loader: 'style-loader'
          },
          {
            loader: 'css-loader'
          },
          {
            loader: 'postcss-loader',
            options: {
              postcssOptions: {
                plugins: () => [
                  require('autoprefixer')
                ]
              }
            }
          },
          {
            loader: 'sass-loader'
          }
        ]
      }
    ]
  }
}