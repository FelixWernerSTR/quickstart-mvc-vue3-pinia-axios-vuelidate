import { decimal, helpers, required, sameAs, minLength, maxLength, minValue, maxValue, numeric, between } from '@vuelidate/validators';

const mustBeCool = (value) => value.includes('cool');

const contains = (param) => (value) => !helpers.req(value) || value.includes(param);

export const useValidation = () => {
  return {
    required: (message: string) => helpers.withMessage(message, required),
    decimal: (message: string) => helpers.withMessage(message, decimal),
    numeric: (message: string) => helpers.withMessage(message, numeric),
    sameAs: (message: string, ...args: Parameters<typeof sameAs>) => helpers.withMessage(message, sameAs(...args)),
    minLength: (message: string, ...args: Parameters<typeof minLength>) => helpers.withMessage(message, minLength(...args)),
    maxLength: (message: string, ...args: Parameters<typeof maxLength>) => helpers.withMessage(message, maxLength(...args)),
    minValue: (message: string, ...args: Parameters<typeof minValue>) => helpers.withMessage(message, minValue(...args)),
    maxValue: (message: string, ...args: Parameters<typeof maxValue>) => helpers.withMessage(message, maxValue(...args)),
    between: (message: string, ...args: Parameters<typeof between>) => helpers.withMessage(message, between(...args)),
    mustBeCool: (message: string) => helpers.withMessage(message, mustBeCool),
    customContains: (message: string, ...args: String) => helpers.withMessage(message, contains(...args)),
    pattern: (message: string, ...args: String) => helpers.withMessage(message, helpers.regex(...args)),
  };
};
