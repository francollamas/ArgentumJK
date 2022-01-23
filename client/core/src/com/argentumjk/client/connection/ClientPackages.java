package com.argentumjk.client.connection;

import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.TimeUtils;
import com.argentumjk.client.general.Commands.EditOptions;
import com.argentumjk.client.general.Config;
import com.argentumjk.client.utils.BytesWritter;
import com.argentumjk.client.utils.Position;

/**
 * Clase con los paquetes que se envían al servidor
 * <p>
 * bytes: corresponde a la secuencia de paquetes que se está armando antes de ser enviada al servidor
 * cola: colección con las secuencais obtenidas en 'bytes' (el socket va leyendo esta cola constantemente)
 * pingTime: usado para medir el tiempo de respuesta del servidor
 */
public class ClientPackages {

    // Enumeración de los paquetes que se envían al servidor
    enum ID {
        LoginExistingChar,
        ThrowDices,
        LoginNewChar,
        Talk,
        Yell,
        Whisper,
        Walk,
        RequestPositionUpdate,
        Attack,
        PickUp,
        CombatModeToggle,
        SafeToggle,
        ResuscitationSafeToggle,
        RequestGuildLeaderInfo,
        RequestAtributes,
        RequestFame,
        RequestSkills,
        RequestMiniStats,
        CommerceEnd,
        UserCommerceEnd,
        BankEnd,
        UserCommerceOk,
        UserCommerceReject,
        Drop,
        CastSpell,
        LeftClick,
        DoubleClick,
        Work,
        UseSpellMacro,
        UseItem,
        CraftBlacksmith,
        CraftCarpenter,
        WorkLeftClick,
        CreateNewGuild,
        SpellInfo,
        EquipItem,
        ChangeHeading,
        ModifySkills,
        Train,
        CommerceBuy,
        BankExtractItem,
        CommerceSell,
        BankDeposit,
        ForumPost,
        MoveSpell,
        MoveBank,
        ClanCodexUpdate,
        UserCommerceOffer,
        GuildAcceptPeace,
        GuildRejectAlliance,
        GuildRejectPeace,
        GuildAcceptAlliance,
        GuildOfferPeace,
        GuildOfferAlliance,
        GuildAllianceDetails,
        GuildPeaceDetails,
        GuildRequestJoinerInfo,
        GuildAlliancePropList,
        GuildPeacePropList,
        GuildDeclareWar,
        GuildNewWebsite,
        GuildAcceptNewMember,
        GuildRejectNewMember,
        GuildKickMember,
        GuildUpdateNews,
        GuildMemberInfo,
        GuildOpenElections,
        GuildRequestMembership,
        GuildRequestDetails,
        Online,
        Quit,
        GuildLeave,
        RequestAccountState,
        PetStand,
        PetFollow,
        TrainList,
        Rest,
        Meditate,
        Resucitate,
        Heal,
        Help,
        RequestStats,
        CommerceStart,
        BankStart,
        Enlist,
        Information,
        Reward,
        RequestMOTD,
        Uptime,
        PartyLeave,
        PartyCreate,
        PartyJoin,
        Inquiry,
        GuildMessage,
        PartyMessage,
        CentinelReport,
        GuildOnline,
        PartyOnline,
        CouncilMessage,
        RoleMasterRequest,
        GMRequest,
        bugReport,
        ChangeDescription,
        GuildVote,
        Punishments,
        ChangePassword,
        Gamble,
        InquiryVote,
        LeaveFaction,
        BankExtractGold,
        BankDepositGold,
        Denounce,
        GuildFundate,
        PartyKick,
        PartySetLeader,
        PartyAcceptMember,
        Ping,


        GMMessage,
        showName,
        OnlineRoyalArmy,
        OnlineChaosLegion,
        GoNearby,
        comment,
        serverTime,
        Where,
        CreaturesInMap,
        WarpMeToTarget,
        WarpChar,
        Silence,
        SOSShowList,
        SOSRemove,
        GoToChar,
        invisible,
        GMPanel,
        RequestUserList,
        Working,
        Hiding,
        Jail,
        KillNPC,
        WarnUser,
        EditChar,
        RequestCharInfo,
        RequestCharStats,
        RequestCharGold,
        RequestCharInventory,
        RequestCharBank,
        RequestCharSkills,
        ReviveChar,
        OnlineGM,
        OnlineMap,
        Forgive,
        Kick,
        Execute,
        BanChar,
        UnbanChar,
        NPCFollow,
        SummonChar,
        SpawnListRequest,
        SpawnCreature,
        ResetNPCInventory,
        CleanWorld,
        ServerMessage,
        NickToIP,
        IPToNick,
        GuildOnlineMembers,
        TeleportCreate,
        TeleportDestroy,
        RainToggle,
        SetCharDescription,
        ForceMIDIToMap,
        ForceWAVEToMap,
        RoyalArmyMessage,
        ChaosLegionMessage,
        CitizenMessage,
        CriminalMessage,
        TalkAsNPC,
        DestroyAllItemsInArea,
        AcceptRoyalCouncilMember,
        AcceptChaosCouncilMember,
        ItemsInTheFloor,
        MakeDumb,
        MakeDumbNoMore,
        DumpIPTables,
        CouncilKick,
        SetTrigger,
        AskTrigger,
        BannedIPList,
        BannedIPReload,
        GuildMemberList,
        GuildBan,
        BanIP,
        UnbanIP,
        CreateItem,
        DestroyItems,
        ChaosLegionKick,
        RoyalArmyKick,
        ForceMIDIAll,
        ForceWAVEAll,
        RemovePunishment,
        TileBlockedToggle,
        KillNPCNoRespawn,
        KillAllNearbyNPCs,
        LastIP,
        ChangeMOTD,
        SetMOTD,
        SystemMessage,
        CreateNPC,
        CreateNPCWithRespawn,
        ImperialArmour,
        ChaosArmour,
        NavigateToggle,
        ServerOpenToUsersToggle,
        TurnOffServer,
        TurnCriminal,
        ResetFactions,
        RemoveCharFromGuild,
        RequestCharMail,
        AlterPassword,
        AlterMail,
        AlterName,
        ToggleCentinelActivated,
        DoBackUp,
        ShowGuildMessages,
        SaveMap,
        ChangeMapInfoPK,
        ChangeMapInfoBackup,
        ChangeMapInfoRestricted,
        ChangeMapInfoNoMagic,
        ChangeMapInfoNoInvi,
        ChangeMapInfoNoResu,
        ChangeMapInfoLand,
        ChangeMapInfoZone,
        SaveChars,
        CleanSOS,
        ShowServerForm,
        night,
        KickAllChars,
        ReloadNPCs,
        ReloadServerIni,
        ReloadSpells,
        ReloadObjects,
        Restart,
        ResetAutoUpdate,
        ChatColor,
        Ignored,
        CheckSlot,
        SetIniVar
    }

    private BytesWritter w;
    private Queue<byte[]> cola;

    private long pingTime;

    public Queue<byte[]> getCola() {
        return cola;
    }

    /**
     * Arma un gran array de bytes, con cada array de la cola y lo devuelve
     */
    public byte[] removeAll() {
        /* Guardo el tamaño para asegurarme que voy a extraer solo lo que hay hasta éste punto, y evitar procesar algo
        que el thread principal me agregue */

        int size = cola.size;

        // Cantidad total de bytes
        int cant = 0;
        for (int i = 0; i < size; i++) {
            cant += cola.get(i).length;
        }

        byte[] totales = new byte[cant];
        int pos = 0;
        for (int i = 0; i < size; i++) {
            byte[] bytes = cola.removeFirst();
            System.arraycopy(bytes, 0, totales, pos, bytes.length);
            pos += bytes.length;
        }

        return totales;
    }

    public long getPingTime() {
        return pingTime;
    }

    public void setPingTime(long pingTime) {
        this.pingTime = pingTime;
    }

    public ClientPackages() {
        w = new BytesWritter();
        cola = new Queue<>();
    }

    /**
     * Agrega el array pendiente a la cola, y lo vacía para mas escritura de paquetes
     */
    public void write() {
        if (w.getSize() > 0) {
            cola.addLast(w.getBytes());
            w.clear();
        }
    }

    /**
     * Petición para conectarse
     */
    public void writeLoginExistingChar(String name, String password) {
        w.writeByte(ID.LoginExistingChar.ordinal());
        w.writeString(name);
        w.writeString(password);
        w.writeByte(0);
        w.writeByte(12);
        w.writeByte(3);
        
        // TODO ACT
        for (int i = 1; i <= 7 ; i++) {
            w.writeShort(0); // 0? u otro valor?
        }
    }

    public void writeThrowDices() {
        w.writeByte(ID.ThrowDices.ordinal());
    }

    public void writeLoginNewChar(String name, String password, String mail, int raza, int sexo, int clase, int ciudad) {
        w.writeByte(ID.LoginNewChar.ordinal());
        w.writeString(name);
        w.writeString(password);
        w.writeByte(0);
        w.writeByte(12);
        w.writeByte(3);

        // TODO ACT
        for (int i = 1; i <= 7 ; i++) {
            w.writeShort(0); // 0? u otro valor?
        }

        w.writeByte(raza);
        w.writeByte(sexo);
        w.writeByte(clase);

        for (int i = 1; i < 22; i++) {
            if (i == 1)
                w.writeByte(10);
            else
                w.writeByte(0);
        }

        w.writeString(mail);
        w.writeByte(ciudad);
    }

    /**
     * Caminar hacia una dirección
     */
    public void writeWalk(Config.Direccion dir) {
        w.writeByte(ID.Walk.ordinal());
        w.writeByte((dir.ordinal() + 1));
    }

    public void writePing() {
        if (getPingTime() != 0) return;
        setPingTime(TimeUtils.millis());
        w.writeByte(ID.Ping.ordinal());
    }

    /**
     * Cambio de dirección sin cambiar de tile
     */
    public void writeChangeHeading(Config.Direccion dir) {
        w.writeByte(ID.ChangeHeading.ordinal());
        w.writeByte(dir.ordinal() + 1);
    }

    /**
     * Petición de posición (para corregirla)
     */
    public void writeRequestPositionUpdate() {
        w.writeByte(ID.RequestPositionUpdate.ordinal());
    }

    public void writeWarpChar(String name, int map, Position pos) {
        w.writeByte(ID.WarpChar.ordinal());
        w.writeString(name);
        w.writeShort(map);
        w.writeByte((byte) pos.getX());
        w.writeByte((byte) pos.getY());
    }

    public void writeLeftClick(Position pos) {
        w.writeByte(ID.LeftClick.ordinal());
        w.writeByte((byte) pos.getX());
        w.writeByte((byte) pos.getY());
    }

    public void writeDoubleClick(Position pos) {
        w.writeByte(ID.DoubleClick.ordinal());
        w.writeByte((byte) pos.getX());
        w.writeByte((byte) pos.getY());
    }

    public void writeOnline() {
        w.writeByte(ID.Online.ordinal());
    }

    public void writeQuit() {
        w.writeByte(ID.Quit.ordinal());
    }

    public void writeTalk(String texto) {
        w.writeByte(ID.Talk.ordinal());
        w.writeString(texto);
    }

    public void writeMeditate() {
        w.writeByte(ID.Meditate.ordinal());
    }

    public void writeResucitate() {
        w.writeByte(ID.Resucitate.ordinal());
    }

    public void writeHeal() {
        w.writeByte(ID.Heal.ordinal());
    }

    public void writeHelp() {
        w.writeByte(ID.Help.ordinal());
    }

    public void writeRequestStats() {
        w.writeByte(ID.RequestStats.ordinal());
    }

    public void writeCommerceStart() {
        w.writeByte(ID.CommerceStart.ordinal());
    }

    public void writeCommerceEnd() {
        w.writeByte(ID.CommerceEnd.ordinal());
    }

    public void writeBankStart() {
        w.writeByte(ID.BankStart.ordinal());
    }

    public void writeEnlist() {
        w.writeByte(ID.Enlist.ordinal());
    }

    public void writeInformation() {
        w.writeByte(ID.Information.ordinal());
    }

    public void writeReward() {
        w.writeByte(ID.Reward.ordinal());
    }

    public void writeUpTime() {
        // w.writeByte(ID.UpTime.ordinal());
    }

    public void writeInquiry() {
        w.writeByte(ID.Inquiry.ordinal());
    }

    public void writeInquiryVote(byte opt) {
        w.writeByte(ID.InquiryVote.ordinal());
        w.writeByte(opt);
    }

    public void writeWarpToMap(short map) {
        // TODO ALT
        // w.writeByte(ID.WarpToMap.ordinal());
        // w.writeShort(map);
    }

    public void writeMoveItem(int slot1, int slot2) {
        // TODO ALT
        /*w.writeByte(ID.MoveItem.ordinal());
        w.writeByte(slot1);
        w.writeByte(slot2);
        w.writeByte(1); // TODO: tipo (inventario, banco, etc...) no se maneja todavía en el servidor...
        */
    }

    public void writeUseItem(int index) {
        w.writeByte(ID.UseItem.ordinal());
        w.writeByte(index);
    }

    public void writeEquipItem(int index) {
        w.writeByte(ID.EquipItem.ordinal());
        w.writeByte(index);
    }

    public void writeTeleportCreate(short map, byte x, byte y, byte radio) {
        w.writeByte(ID.TeleportCreate.ordinal());
        w.writeShort(map);
        w.writeByte(x);
        w.writeByte(y);
        w.writeByte(radio);
    }

    public void writeTeleportDestroy() {
        w.writeByte(ID.TeleportDestroy.ordinal());
    }

    public void writeEditChar(String nombre, EditOptions caract, String arg1, String arg2) {
        w.writeByte(ID.EditChar.ordinal());
        w.writeString(nombre);
        w.writeByte(caract.ordinal() + 1);
        w.writeString(arg1);
        w.writeString(arg2);
    }

    public void writeSummonChar(String nombre) {
        w.writeByte(ID.SummonChar.ordinal());
        w.writeString(nombre);
    }

    public void writeGoToChar(String nombre) {
        w.writeByte(ID.GoToChar.ordinal());
        w.writeString(nombre);
    }

    public void writeServerMessage(String nombre) {
        w.writeByte(ID.ServerMessage.ordinal());
        w.writeString(nombre);
    }

    public void writeCreateItem(short index, short cantidad) {
        w.writeByte(ID.CreateItem.ordinal());
        w.writeShort(index);
        w.writeShort(cantidad);
    }

    public void writeDestroyItems() {
        w.writeByte(ID.DestroyItems.ordinal());
    }

    public void writeSearchObjs(String texto) {
        // TODO ALT
        // w.writeByte(ID.SearchObjs.ordinal());
        // w.writeString(texto);
    }

    public void writeNpcFollow() {
        w.writeByte(ID.NPCFollow.ordinal());
    }

    public void writeInvisible() {
        // TODO ALT
        // w.writeByte(ID.Invisible.ordinal());
    }

}