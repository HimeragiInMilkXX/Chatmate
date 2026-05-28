
import * as z from "zod"

export const LoginSchema = z.object( {

  usernameOrEmail: z.string( "Invalid Identifier" ).nonempty( "Username or Email is required" ),
  password: z.string( "Invalid Password" ).nonempty( "Password is required" )

} );

export const RegisterSchema = z.object({
    email: z.string()
      .regex(/^[^\s@]+@[^\s@]+\.[^\s@]+$/, "Please provide a valid email"),
    username: z.string().max(16, "Username must be less than 16 characters"),
    password: z.string().min(8, "Password must be at least 8 characters")
      .regex(/[a-z]/, "Must contain a lowercase letter")
      .regex(/[A-Z]/, "Must contain an uppercase letter")
      .regex(/[0-9]/, "Must contain a number")
      .regex(/[!@#$%^&*(),.?":{}|<>]/, "Must contain a symbol"),
    confirmPassword: z.string()
  })
  .refine( data => data.password === data.confirmPassword, {

      message: "Passwords do not match",
      path: ["confirmPassword"]

  })

export const ProfileSchema = z.object({

  username: z.string().max(16, "Username must be less than 16 characters").nonempty().regex(/^\S*$/, "Spaces are not allowed"),

  // ADD VALIDATION RULES FOR PROFILE FIELDS
  age: z.number().min(0, "Age must be a positive number").max(120, "Age must be less than 120").nullable(),
  bio: z.string().max(500, "Bio must be less than 500 characters").nullable(),
  birthday: z.string().min(0, "Birthday must be a valid timestamp").max(Date.now(), "Birthday cannot be in the future").regex(/^\S*$/, "Spaces are not allowed").nullable(),
  nationality: z.string().max(20, "Nationality must be less than 20 characters").nullable(),
  region: z.string().max(20, "Region must be less than 20 characters").nullable(),
  interest: z.string().max(100, "Interest must be less than 100 characters").nullable(),

})

export const ResetPasswordSchema = z.object({
    oldPassword: z.string( "Invalid Password" ).nonempty( "Old Password is required" ),
    newPassword: z.string().min(8, "Password must be at least 8 characters")
      .regex(/[a-z]/, "Must contain a lowercase letter")
      .regex(/[A-Z]/, "Must contain an uppercase letter")
      .regex(/[0-9]/, "Must contain a number")
      .regex(/[!@#$%^&*(),.?":{}|<>]/, "Must contain a symbol"),
    confirmPassword: z.string()
  })
  .refine( data => data.newPassword === data.confirmPassword, {

      message: "Passwords do not match",
      path: ["confirmPassword"]

  })
