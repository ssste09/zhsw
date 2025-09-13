"use client";
import { FunctionComponent, useState } from "react";
import { useLogin } from "../../utils/hooks";
import { Field, Form, Formik } from "formik";
import { LoginFormData } from "../../utils/types";

const initialValues: LoginFormData = {
  email: "",
  password: ""
};
const LoginCard: FunctionComponent = () => {
  const [showPw, setShowPw] = useState(false);
  const { trigger, isMutating, error } = useLogin();

  const handleSubmit = (values: LoginFormData) => {
    return trigger({ email: values.email!, password: values.password! }).then(
      () => {
        console.log("success");
      }
    );
  };

  return (
    <div className="w-full max-w-md rounded-2xl border border-zinc-200/60 bg-white/90 p-8 shadow-lg backdrop-blur dark:border-zinc-800 dark:bg-zinc-900/80">
      <div className="mb-6">
        <h1 className="text-2xl font-semibold tracking-tight text-zinc-900 dark:text-zinc-100">
          Login
        </h1>
        <p className="mt-1 text-sm text-zinc-600 dark:text-zinc-400">
          Enter your credentials
        </p>
      </div>
      {error && (
        <div
          role="alert"
          className="mb-4 rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700 dark:border-red-900/50 dark:bg-red-950/60 dark:text-red-300"
        >
          {error instanceof Error ? error.message : String(error)}
        </div>
      )}
      <Formik initialValues={initialValues} onSubmit={handleSubmit}>
        {({ isSubmitting, status }) => (
          <Form className="space-y-4">
            {status && (
              <div
                role="alert"
                className="rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700 dark:border-red-900/50 dark:bg-red-950/60 dark:text-red-300"
              >
                {status}
              </div>
            )}

            <div>
              <label
                htmlFor="username"
                className="mb-1 block text-sm font-medium text-zinc-800 dark:text-zinc-200"
              >
                Username
              </label>
              <Field
                id="email"
                name="email"
                autoComplete="email"
                className="block w-full rounded-xl border border-zinc-300 bg-white px-3 py-2 text-zinc-900 outline-none placeholder:text-zinc-400 focus:border-zinc-400 focus-visible:outline  focus-visible:outline-zinc-500 dark:border-zinc-700 dark:bg-zinc-950 dark:text-zinc-100"
                placeholder="jane.doe"
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="mb-1 block text-sm font-medium text-zinc-800 dark:text-zinc-200"
              >
                Password
              </label>
              <div className="relative">
                <Field
                  id="password"
                  name="password"
                  type={showPw ? "text" : "password"}
                  autoComplete="current-password"
                  className="block w-full rounded-xl border border-zinc-300 bg-white px-3 py-2 pr-10 text-zinc-900 outline-none placeholder:text-zinc-400 focus:border-zinc-400 focus-visible:outline  focus-visible:outline-zinc-500 dark:border-zinc-700 dark:bg-zinc-950 dark:text-zinc-100"
                  placeholder="••••••••"
                />
                <button
                  type="button"
                  onClick={() => setShowPw((s) => !s)}
                  className="absolute inset-y-0 right-2 my-auto rounded-md px-2 text-sm text-zinc-600 hover:text-zinc-800 focus-visible:outline focus-visible:outline-zinc-500 dark:text-zinc-400 dark:hover:text-zinc-200"
                  aria-label={showPw ? "Hide password" : "Show password"}
                >
                  {showPw ? "Hide" : "Show"}
                </button>
              </div>
            </div>

            <button
              type="submit"
              disabled={isSubmitting || isMutating}
              className="inline-flex w-full items-center justify-center rounded-xl bg-zinc-900 px-4 py-2.5 text-sm font-medium text-white transition active:translate-y-px disabled:opacity-60 dark:bg-zinc-100 dark:text-zinc-900"
            >
              {isSubmitting || isMutating ? "Signing in…" : "Sign in"}
            </button>
          </Form>
        )}
      </Formik>
    </div>
  );
};

export default LoginCard;
