export class Angebot {
  constructor(
    public id?: number,
    public name?: string | null,
    public angebotsnummer?: number | null,
    public schlagwort?: string | null,
    public versicherungsbeginn?: string | null,
    public buJN?: boolean | null
  ) {}
}
