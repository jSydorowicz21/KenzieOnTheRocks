const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const {defaults} = require("autoprefixer");
const NodePolyfillPlugin = require("node-polyfill-webpack-plugin");

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    landingPage: path.resolve(__dirname, 'src', 'pages', 'landingPage.js'),
    loginPage: path.resolve(__dirname, 'src', 'pages', 'loginPage.js'),
    drinkPage: path.resolve(__dirname, 'src', 'pages', 'drinkPage.js'),
  },
  module: {
    rules: [
      {
        test: /\.(js)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            "presets": [
                ["@babel/preset-react", {targets: "defaults"}]
            ]
          }
        }
      },
      {
          test: /.(jsx)$/,
          exclude: /node_modules/,
          use: {
        loader: 'babel-loader',
        options: {
          "presets": [
            ["@babel/preset-react", {targets: "defaults"}]
          ]
        }
      }
    },
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
    },
    {
      test: /\.(css)$/,
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
  },
  resolve: {
    extensions: ['*', '.js']
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
    static: path.resolve(__dirname, './dist'),
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
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/util/images'),
          to: path.resolve("dist/images")
        }
      ]
    }),
    new CleanWebpackPlugin(),
    new NodePolyfillPlugin(), // Polyfill Node.js globals (e.g. global, process, etc)
  ]
}