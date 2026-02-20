module.exports = function (api) {
  api.cache(true);
  return {
    presets: ["babel-preset-expo"],
    plugins: [
      [
        "module-resolver",
        {
          alias: {
            // 👇 point directly at the built dist folder in libs
            "@myorg/auth_api": "../../libs/auth_api/dist",
            "@myorg/apis_api": "../../libs/apis_api/dist",
          },
        },
      ],
    ],
  };
};
