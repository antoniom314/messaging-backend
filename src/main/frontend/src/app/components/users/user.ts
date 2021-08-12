export interface User {

  username: string;
  password: string;
  socketSessionId: string;
  active: number;
  roles: string;
  permissions: string;
}
