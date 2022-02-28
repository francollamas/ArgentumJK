package com.argentumjk.client.connection;

import com.argentumjk.client.general.IMidiPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.TimeUtils;
import com.argentumjk.client.actors.Item;
import com.argentumjk.client.utils.*;
import com.argentumjk.client.views.windows.ComerciarWindow;
import com.argentumjk.client.actors.Consola;
import com.argentumjk.client.containers.Assets;
import com.argentumjk.client.containers.Colors;
import com.argentumjk.client.containers.FontTypes;
import com.argentumjk.client.containers.GameData;
import com.argentumjk.client.general.Config;
import com.argentumjk.client.Game;
import com.argentumjk.client.general.Messages;
import com.argentumjk.client.graphics.Grh;
import com.argentumjk.client.objects.*;
import com.argentumjk.client.general.Config.Direccion;
import com.argentumjk.client.general.Messages.Message;
import com.argentumjk.client.containers.FontTypes.FontTypeName;
import com.argentumjk.client.views.screens.PrincipalView;
import com.argentumjk.client.views.screens.PrincipalViewM;
import com.argentumjk.client.views.screens.View;

import static com.badlogic.gdx.Application.ApplicationType.Desktop;
import static com.badlogic.gdx.Application.ApplicationType.WebGL;
import static com.argentumjk.client.containers.GameData.*;


/**
 * Clase con los paquetes que vienen del servidor y el cliente tiene que procesar
 * <p>
 * cola: conjunto de secuencias de paquetes (el socket va ingresando las secuencias constantemente).
 */
public class ServerPackages {

    // Enumeración de los paquetes que se reciben del servidor
    enum ID {
        Logged,
        RemoveDialogs,
        RemoveCharDialog,
        NavigateToggle,
        Disconnect,
        CommerceEnd,
        BankEnd,
        CommerceInit,
        BankInit,
        UserCommerceInit,
        UserCommerceEnd,
        ShowBlacksmithForm,
        ShowCarpenterForm,
        NPCSwing,
        NPCKillUser,
        BlockedWithShieldUser,
        BlockedWithShieldOther,
        UserSwing,
        UpdateNeeded,
        SafeModeOn,
        SafeModeOff,
        ResuscitationSafeOn,
        ResuscitationSafeOff,
        NobilityLost,
        CantUseWhileMeditating,
        UpdateSta,
        UpdateMana,
        UpdateHP,
        UpdateGold,
        UpdateExp,
        ChangeMap,
        PosUpdate,
        NPCHitUser,
        UserHitNPC,
        UserAttackedSwing,
        UserHittedByUser,
        UserHittedUser,
        ChatOverHead,
        ConsoleMsg,
        GuildChat,
        ShowMessageBox,
        UserIndexInServer,
        UserCharIndexInServer,
        CharacterCreate,
        CharacterRemove,
        CharacterChangeNick,
        CharacterMove,
        ForceCharMove,
        CharacterChange,
        ObjectCreate,
        ObjectDelete,
        BlockPosition,
        PlayMIDI,
        PlayWave,
        guildList,
        AreaChanged,
        PauseToggle,
        RainToggle,
        CreateFX,
        UpdateUserStats,
        WorkRequestTarget,
        ChangeInventorySlot,
        ChangeBankSlot,
        ChangeSpellSlot,
        Atributes,
        BlacksmithWeapons,
        BlacksmithArmors,
        CarpenterObjects,
        RestOK,
        ErrorMsg,
        Blind,
        Dumb,
        ShowSignal,
        ChangeNPCInventorySlot,
        UpdateHungerAndThirst,
        Fame,
        MiniStats,
        LevelUp,
        AddForumMsg,
        ShowForumForm,
        SetInvisible,
        DiceRoll,
        MeditateToggle,
        BlindNoMore,
        DumbNoMore,
        SendSkills,
        TrainerCreatureList,
        guildNews,
        OfferDetails,
        AlianceProposalsList,
        PeaceProposalsList,
        CharacterInfo,
        GuildLeaderInfo,
        GuildDetails,
        ShowGuildFundationForm,
        ParalizeOK,
        ShowUserRequest,
        TradeOK,
        BankOK,
        ChangeUserTradeSlot,
        SendNight,
        Pong,
        UpdateTagAndStatus,
        SpawnList,
        ShowSOSForm,
        ShowMOTDEditionForm,
        ShowGMPanelForm,
        UserNameList
    }


    private boolean lostConnection;
    private BytesReader r;
    private Queue<byte[]> cola;

    public boolean isLostConnection() {
        return lostConnection;
    }

    public void setLostConnection(boolean lostConnection) {
        this.lostConnection = lostConnection;
    }

    public Queue<byte[]> getCola() {
        return cola;
    }

    public ServerPackages() {
        r = new BytesReader();
        r.setLittleEndian(true);
        cola = new Queue<byte[]>();
    }

    public String bu(String key) {
        return Game.getInstance().getBundle().get(key);
    }

    public GameData getGD() {
        return Game.getInstance().getGameData();
    }

    public Assets getAssets() {
        return Game.getInstance().getAssets();
    }

    public IMidiPlayer getMidiPlayer() {
        return Game.getInstance().getMidiPlayer();
    }

    private Stage getActStage() {
        return ((View) Game.getInstance().getScreen()).getStage();
    }

    /**
     * Lee los paquetes almacenados en la cola.
     */
    public void doActions() {
        int size = cola.size;
        for (int i = 0; i < size; i++)
            handleReceived(cola.removeFirst());
    }

    /**
     * Ejecuta los métodos correspondientes al ID de los paquetes recibidos.
     */
    public void handleReceived(byte[] bytes) {
        /*
        * Esta variable se activa si se recibe un paquete que no existe
        * (para que deje de procesar paquetes, sino se rompe el juego)
        */
        boolean broken = false;

        r.appendBytes(bytes);

        try {
            while (r.getAvailable() > 0 && !broken) {

                // Marco la posición del comienzo del paquete
                r.mark();

                ID id = ID.values()[r.readByte()];
                //Gdx.app.log(""+id.ordinal(), id.name());
                //System.out.println(id);

                switch (id) {
                    case CreateFX:
                        handleCreateFX();
                        break;
                    case ChangeInventorySlot:
                        handleChangeInventorySlot();
                        break;
                    case ChangeNPCInventorySlot:
                        handleChangeNPCInventorySlot();
                        break;
                    case ChangeSpellSlot:
                        handleChangeSpellSlot();
                        break;
                    case Dumb:
                        handleDumb();
                        break;
                    case DumbNoMore:
                        handleDumbNoMore();
                        break;
                    case Blind:
                        handleBlind();
                        break;
                    case BlindNoMore:
                        handleBlindNoMore();
                        break;
                    case UserIndexInServer:
                        handleUserIndexInServer();
                        break;
                    case ChangeMap:
                        handleChangeMap();
                        break;
                    case PlayMIDI:
                        handlePlayMusic();
                        break;
                    case AreaChanged:
                        handleAreaChanged();
                        break;
                    case CharacterCreate:
                        handleCharacterCreate();
                        break;
                    case CharacterChange:
                        handleCharacterChange();
                        break;
                    case UserCharIndexInServer:
                        handleUserCharIndexInServer();
                        break;
                    case UpdateUserStats:
                        handleUpdateUserStats();
                        break;
                    case UpdateHungerAndThirst:
                        handleUpdateHungerAndThirst();
                        break;
                    // TODO ACT
                    /*case UpdateStrenghtAndDexterity:
                        handleUpdateStrenghtAndDexterity();
                        break;
                    case UpdateStrenght:
                        handleUpdateStrenght();
                        break;
                    case UpdateDexterity:
                        handleUpdateDexterity();
                        break;
                    */
                    case SendSkills:
                        handleSendSkills();
                        break;
                    case LevelUp:
                        handleLevelUp();
                        break;
                    case Logged:
                        handleLogged();
                        break;
                    case ErrorMsg:
                        handleErrorMsg();
                        break;
                    case ShowMessageBox:
                        handleShowMessageBox();
                        break;
                    case ObjectCreate:
                        handleObjectCreate();
                        break;
                    case ObjectDelete:
                        handleObjectDelete();
                        break;
                    case BlockPosition:
                        handleBlockPosition();
                        break;
                    case CharacterMove:
                        handleCharacterMove();
                        break;
                    case PosUpdate:
                        handlePosUpdate();
                        break;
                    case ChatOverHead:
                        handleChatOverHead();
                        break;
                    case ConsoleMsg:
                        handleConsoleMsg();
                        break;
                    case CharacterRemove:
                        handleCharacterRemove();
                        break;
                    case ForceCharMove:
                        handleForceCharMove();
                        break;
                    case RemoveDialogs:
                        handleRemoveDialogs();
                        break;
                    case PlayWave:
                        handlePlaySound();
                        break;
                    case RemoveCharDialog:
                        handleRemoveCharDialog();
                        break;
                    case UpdateSta:
                        handleUpdateSta();
                        break;
                    case UpdateMana:
                        handleUpdateMana();
                        break;
                    case UpdateHP:
                        handleUpdateHP();
                        break;
                    case UpdateGold:
                        handleUpdateGold();
                        break;
                    case UpdateExp:
                        handleUpdateExp();
                        break;
                    case DiceRoll:
                        handleDiceRoll();
                        break;
                    case Pong:
                        handlePong();
                        break;
                    case Disconnect:
                        handleDisconnect();
                        break;
                    case NavigateToggle:
                        handleNavigateToggle();
                        break;
                    case PauseToggle:
                        handlePauseToggle();
                        break;
                    // TODO ACT
                    //case MultiMessage:
                        //handleMultiMessage();
                        //break;
                    case MeditateToggle:
                        handleMeditateToggle();
                        break;
                    case CommerceInit:
                        handleCommerceInit();
                        break;
                    case CommerceEnd:
                        handleCommerceEnd();
                        break;
                    case SetInvisible:
                        handleSetInvisible();
                        break;
                    case ResuscitationSafeOff:
                        handleResuscitationSafeOff();
                        break;
                    case ResuscitationSafeOn:
                        handleResuscitationSafeOn();
                        break;
                    // TODO ACT
                    //case UpdateUsersOnline:
                        //handleUpdateUsersOnline();
                        //break;
                    case GuildChat:
                        handleGuildChat();
                        break;
                    case SafeModeOn:
                        handleSafeModeOn();
                        break;
                    case SafeModeOff:
                        handleSafeModeOff();
                        break;
                    default:
                        System.out.println("Paquete no encontrado: " + id);
                        // Si llega un paquete que no está implementado...
                        Dialogs.showOKDialog(bu("error"), "Paquete no implementado: " + id.ordinal() + " '" + id.toString() + "'.");
                        broken = true;
                        break;
                }
            }

            r.clear();
        }
        catch (NotEnoughDataException ex) {
            /* Es común que un paquete llegue cortado, por lo que no hay suficientes datos para leer...
            entonces se vuelve hasta la posición marcada (comienzo del último paquete)
            */
            r.reset();
        }
    }

    /**
     * Asigna un FX a un char
     */
    public void handleCreateFX() throws NotEnoughDataException {
        short charIndex = r.readShort();
        short fxIndex = r.readShort();
        short loops = r.readShort();

        Char c = getGD().getChars().getChar(charIndex);
        if (c == null) return;
        c.setFx(fxIndex, loops);
    }


    public void handleChangeInventorySlot() throws NotEnoughDataException {
        Item item = new Item();
        int index = r.readByte();

        item.set(r.readShort(), r.readString(), r.readShort(), r.readBoolean(),
                    r.readShort(), r.readByte(),
                    r.readShort(), r.readShort(), r.readShort(), r.readFloat());
        //if (index == 2) item.setChecked(true);

        getGD().getInventario().setSlot(item, index);
    }

    public void handleChangeNPCInventorySlot() throws NotEnoughDataException {
        //OLDItem item = new OLDItem();
        r.readByte();
        r.readString();
        r.readShort();
        r.readFloat();
        r.readShort();
        r.readShort();
        r.readByte();
        r.readShort();
        r.readShort();
        r.readShort();
        r.readShort();
    }

    private void handleChangeSpellSlot() throws NotEnoughDataException {
        // TODO: completar
        r.readByte();
        r.readShort();
        r.readString();
    }

    private void handleDumb() {
        getGD().getCurrentUser().setEstupido(true);
    }

    private void handleDumbNoMore() {
        getGD().getCurrentUser().setEstupido(false);
    }

    private void handleBlind() {
        getGD().getCurrentUser().setCiego(true);
    }

    private void handleBlindNoMore() {
        getGD().getCurrentUser().setCiego(false);
    }

    /**
     * Guarda el index con el que el servidor conoce usuario que estámos manejando
     */
    private void handleUserIndexInServer() throws NotEnoughDataException {
        getGD().getCurrentUser().setIndex(r.readShort());
    }

    private void handleChangeMap() throws NotEnoughDataException {
        getGD().getCurrentUser().setMap(r.readShort());

        // Version del mapa (por ahora no se usa)
        r.readShort();

        getAssets().changeMap(getGD().getCurrentUser().getMap());
        getGD().getChars().clear();
        // TODO: manejar la lluvia.. (si no hay, parar el sonido)
    }

    private void handlePlayMusic() throws NotEnoughDataException {
        int num = r.readByte();
        if (num > 0)
            getMidiPlayer().play(num);

        // Corresponde a los loops, pero considero que todas las músicas se repiten infinitamente
        r.readShort();
    }


    /**
     * Define la nueva área, y borra todos los personajes y objetos que no pertenecen a esa área
     */
    private void handleAreaChanged() throws NotEnoughDataException {
        // Los valores están hardcodeados
        Rect area = getGD().getCurrentUser().getArea();
        int x = r.readByte();
        int y = r.readByte();

        area.setX1((x / 9 - 1) * 9);
        area.setWidth(26);

        area.setY1((y / 9 - 1) * 9);
        area.setHeight(26);

        for (int i = 1; i <= 100; i++) {
            for (int j = 1; j <= 100; j++) {
                if (!area.isPointIn(new Position(i, j))) {
                    // Borro usuarios y npcs.
                    MapTile tile = getAssets().getMapa().getTile(i, j);
                    if (tile.getCharIndex() > 0) {
                        if (tile.getCharIndex() != getGD().getCurrentUser().getIndexInServer()) {
                            getGD().getChars().deleteChar(tile.getCharIndex());
                        }
                    }

                    // Borro objetos
                    tile.setObjeto(null);
                }
            }
        }

        getGD().getChars().refresh();
    }

    /**
     * Crea un PJ o NPC, asignándole todas sus características y lo ubica en una posición del mapa
     */
    private void handleCharacterCreate() throws NotEnoughDataException {
        int index = r.readShort();
        Char c = getGD().getChars().getChar(index, true);

        c.setBody(r.readShort());
        c.setHead(r.readShort());
        c.setHeading(Config.Direccion.values()[r.readByte() - 1]);
        c.getPos().set(r.readByte(), r.readByte());
        c.setWeapon(r.readShort());
        c.setShield(r.readShort());
        c.setHelmet(r.readShort());

        // Lecturas innecesarias (fx y loop, pero no se usan)
        r.readShort();
        r.readShort();

        c.setNombre(r.readString());
        // TODO ACT setear Guild, extrayendolo del nombre
        //c.setGuildName(r.readString());
        c.setBando(r.readByte());

        // Privilegios
        int privs = r.readByte();
        if (privs != 0) {
            // Si es del concejo del caos y tiene privilegios
            if ((privs & 64) != 0 && (privs & 1) == 0)
                privs = (byte) (privs ^ 64);

            // Si es del concejo de banderbill y tiene privilegios
            if ((privs & 128) != 0 && (privs & 1) == 0)
                privs = (byte) (privs ^ 128);

            // Si es rolmaster
            if ((privs & 32) != 0)
                privs = 32;

            // Con ésta operación se obtiene el número correspondiente al privilegio del usuario y se le asigna.
            c.setPriv((int) (Math.log(privs) / Math.log(2)));
        } else
            c.setPriv(0);

        // Actualizamos el atributo lastChar. (para saber cual es el index del char con nro mas alto)
        if (index > getGD().getChars().getLastChar()) getGD().getChars().setLastChar(index);

        // Si el char es nuevo, aumento la cantidad de chars.
        if (!c.isActive()) getGD().getChars().setNumChars(getGD().getChars().getNumChars() + 1);

        // Lo activamos e insertamos en el mapa
        c.setActive(true);
        getAssets().getMapa().getTile((int) c.getPos().getX(), (int) c.getPos().getY()).setCharIndex(index);
        getGD().getChars().refresh();
    }

    /**
     * Actualiza las características del PJ o NPC ya existente.
     */
    private void handleCharacterChange() throws NotEnoughDataException {
        int index = r.readShort();
        Char c = getGD().getChars().getChar(index);

        c.setBody(r.readShort());
        c.setHead(r.readShort());
        c.setMuerto(c.getHeadIndex() == MUERTO_HEAD);
        c.setHeading(Direccion.values()[r.readByte() - 1]);
        c.setWeapon(r.readShort());
        c.setShield(r.readShort());
        c.setHelmet(r.readShort());
        c.setFx(r.readShort(), r.readShort());

        User u = getGD().getCurrentUser();
        if (index == u.getIndexInServer() && u.isCambiandoDir())
            u.setCambiandoDir(false);

        getGD().getChars().refresh();
    }

    /**
     * Borra un PJ o NPC
     */
    private void handleCharacterRemove() throws NotEnoughDataException {
        int index = r.readShort();
        getGD().getChars().deleteChar(index);
        getGD().getChars().refresh();
    }

    /**
     * Mueve a cualquier PJ o NPC
     */
    private void handleCharacterMove() throws NotEnoughDataException {
        getGD().getChars().moveChar(r.readShort(), r.readByte(), r.readByte());
        getGD().getChars().refresh();
    }

    /**
     * Mueve al PJ actual a una dirección especificada (el servidor fuerza el movimiento de nuestro PJ)
     */
    private void handleForceCharMove() throws NotEnoughDataException {
        Direccion dir = Direccion.values()[r.readByte() - 1];
        User u = getGD().getCurrentUser();
        getGD().getChars().moveChar(u.getIndexInServer(), dir);
        getGD().getWorld().setMove(dir);
        getGD().getChars().refresh();
    }

    /**
     * Actualiza la posición del usuario (en caso que esté incorrecta)
     */
    private void handlePosUpdate() throws NotEnoughDataException {
        // Obtengo la posición real del personaje
        int x = r.readByte();
        int y = r.readByte();

        // Obtengo la posición posiblemente incorrecta
        Position wPos = getGD().getWorld().getPos();

        // Si estaba bien, salgo
        if (wPos.equals(new Position(x, y))) return;

        // Si estaba mal:
        // Borro el char de esa pos del mapa
        getAssets().getMapa().getTile((int) wPos.getX(), (int) wPos.getY()).setCharIndex(0);

        // Cambio las coordenadas del World
        wPos.set(x, y);

        // Pongo al char en donde debe ir realmente
        User u = getGD().getCurrentUser();
        getAssets().getMapa().getTile(x, y).setCharIndex(u.getIndexInServer());

        // TODO ACT arreglar si o si.. es la POs
        Position cPos = getGD().getChars().getChar(u.getIndexInServer()).getPos();
        cPos.set(x, y);

        getGD().getWorld().setTecho();
    }

    /**
     * Asigna el índice del usuario principal y cambia la posición del mundo
     */
    private void handleUserCharIndexInServer() throws NotEnoughDataException {
        int index = r.readShort();
        getGD().getCurrentUser().setIndexInServer(index);

        Char c = getGD().getChars().getChar(index);
        if (c != null) {
            Position p = c.getPos();
            getGD().getWorld().getPos().set(p.getX(), p.getY());
            getGD().getWorld().setTecho();
        }
    }

    private void handleUpdateUserStats() throws NotEnoughDataException {
        UserStats s = getGD().getCurrentUser().getStats();
        s.setMaxVida(r.readShort());
        s.setVida(r.readShort());
        s.setMaxMana(r.readShort());
        s.setMana(r.readShort());
        s.setMaxEnergia(r.readShort());
        s.setEnergia(r.readShort());
        s.setOro(r.readInt());
        s.setNivel(r.readByte());
        s.setMaxExp(r.readInt());
        s.setExp(r.readInt());
    }

    private void handleUpdateHungerAndThirst() throws NotEnoughDataException {
        UserStats s = getGD().getCurrentUser().getStats();
        s.setMaxSed(r.readByte());
        s.setSed(r.readByte());
        s.setMaxHambre(r.readByte());
        s.setHambre(r.readByte());
    }

    private void handleUpdateStrenghtAndDexterity() throws NotEnoughDataException {
        UserStats s = getGD().getCurrentUser().getStats();
        s.setFuerza(r.readByte());
        s.setAgilidad(r.readByte());
    }

    private void handleUpdateStrenght() throws NotEnoughDataException {
        getGD().getCurrentUser().getStats().setFuerza(r.readByte());
    }

    private void handleUpdateDexterity() throws NotEnoughDataException {
        getGD().getCurrentUser().getStats().setAgilidad(r.readByte());
    }

    private void handleSendSkills() throws NotEnoughDataException {
        // TODO: hacerlo.. LEE CLASE, Y LEE CADA SKILL
        r.readByte();
        for (int i = 0; i < 22; i++) {
            r.readByte();
        }
    }

    private void handleLevelUp() throws NotEnoughDataException {
        // TODO: revisar
        r.readShort();
    }

    /**
     * Carga la pantalla principal
     */
    private void handleLogged() {
        if (Gdx.app.getType() == Desktop || Gdx.app.getType() == WebGL)
            Game.getInstance().setScreen(new PrincipalView());
        else
            Game.getInstance().setScreen(new PrincipalViewM());
    }

    /**
     * Muestra un mensaje de error
     */
    private void handleErrorMsg() throws NotEnoughDataException {
        Dialogs.showOKDialog(bu("error"), r.readString());
    }

    /**
     * Muestra un mensaje del servidor
     */
    private void handleShowMessageBox() throws NotEnoughDataException {
        Dialogs.showOKDialog(bu("msg.sv"), r.readString());
    }

    /**
     * Crea un objeto en el mapa
     */
    private void handleObjectCreate() throws NotEnoughDataException {
        MapTile tile = getAssets().getMapa().getTile(r.readByte(), r.readByte());
        tile.setObjeto(new Grh(r.readShort()));
    }

    /**
     * Borra un objeto del mapa
     */
    private void handleObjectDelete() throws NotEnoughDataException {
        MapTile tile = getAssets().getMapa().getTile(r.readByte(), r.readByte());
        tile.setObjeto(null);
    }

    /**
     * Bloquea o desbloquea una posición del mapa
     */
    private void handleBlockPosition() throws NotEnoughDataException {
        MapTile tile = getAssets().getMapa().getTile(r.readByte(), r.readByte());
        tile.setBlocked(r.readBoolean());
    }

    /**
     * Agrega el diálogo de un PJ
     */
    private void handleChatOverHead() throws NotEnoughDataException {
        String texto = r.readString().trim();
        int index = r.readShort();
        Color c = Colors.newColor(r.readByte(), r.readByte(), r.readByte());

        getGD().getChars().setDialog(index, texto, c);
    }

    /**
     * Agrega un mensaje en consola
     */
    private void handleConsoleMsg() throws NotEnoughDataException {
        getGD().getConsola().addMessage(r.readString(), FontTypes.FontTypeName.values()[r.readByte()]);
        // TODO: si surge algún error, manejar también las fonts con formato viejo (ej ~255~255~255~1~0~)
    }

    /**
     * Reproduce un sonido
     */
    private void handlePlaySound() throws NotEnoughDataException {
        getAssets().getAudio().playSound(r.readByte());

        // Estos valores corresponden a la posición (X, Y) de donde proviene el sonido (para sonido 3D, que no se usa)
        r.readByte();
        r.readByte();
    }

    private void handleRemoveDialogs() {
        // TODO: Borrar diálogos
        // y esto borraría todos los diálogos??? (fijarme que hacer)
    }

    /**
     * Borra el dialogo de un PJ
     */
    private void handleRemoveCharDialog() throws NotEnoughDataException {
        int index = r.readShort();

        // TODO: llamar a RemoveDialog(index)
        // esto es realmente necesario? (fijarme).. ya que como el diálogo es un atributo del char, si borro el char se pierde el diálogo
    }

    private void handleDisconnect() {
        getGD().disconnect();
    }

    public void handleUpdateSta() throws NotEnoughDataException {
        getGD().getCurrentUser().getStats().setEnergia(r.readShort());
    }

    public void handleUpdateMana() throws NotEnoughDataException {
        getGD().getCurrentUser().getStats().setMana(r.readShort());
    }

    public void handleUpdateHP() throws NotEnoughDataException {
        User u = getGD().getCurrentUser();
        u.getStats().setVida(r.readShort());
    }

    public void handleUpdateGold() throws NotEnoughDataException {
        getGD().getCurrentUser().getStats().setOro(r.readInt());
    }

    public void handleUpdateExp() throws NotEnoughDataException {
        getGD().getCurrentUser().getStats().setExp(r.readInt());
    }

    public void handleDiceRoll() throws NotEnoughDataException {
        UserAtributos a = getGD().getCurrentUser().getAtributos();
        a.setFuerza(r.readByte());
        a.setAgilidad(r.readByte());
        a.setInteligencia(r.readByte());
        a.setCarisma(r.readByte());
        a.setConstitucion(r.readByte());
    }

    public void handlePong() {
        ClientPackages c = Game.getInstance().getConnection().getClPack();
        int ping = (int) (TimeUtils.millis() - c.getPingTime() - (Gdx.graphics.getRawDeltaTime() * 1000));
        c.setPingTime(0);

        getGD().getConsola().addMessage("El ping es de " + ping + " ms.", FontTypes.FontTypeName.Warning);
    }

    public void handleNavigateToggle() {
        getGD().getCurrentUser().setNavegando(!getGD().getCurrentUser().isNavegando());
    }

    public void handlePauseToggle() {
        getGD().setPausa(!getGD().isPausa());
    }

    public void handleMultiMessage() throws NotEnoughDataException {

        Message msg = Messages.Message.values()[r.readByte()];
        Consola c = getGD().getConsola();
        I18NBundle b = Game.getInstance().getBundle();

        switch (msg) {
            case DontSeeAnything:
                c.addMessage(b.get("msg.dont-see-anything"), FontTypeName.Info);
                break;

            // TODO: completar con todos los mensajes!!!
        }
    }

    private void handleMeditateToggle() {
        getGD().getCurrentUser().setMeditando(!getGD().getCurrentUser().isMeditando());
    }

    private void handleCommerceInit() {
        // TODO: completar
        // llenar el NPCInventory, mostrar la pantalla
        ((View) Game.getInstance().getScreen()).getStage().addActor(new ComerciarWindow());
        getGD().getCurrentUser().setComerciando(true);
    }

    private void handleCommerceEnd() {
        // cerrar la pantalla y cambiar Flag comerciando
        getGD().getCurrentUser().setComerciando(false);
    }

    private void handleSetInvisible() throws NotEnoughDataException {
        Char c = getGD().getChars().getChar(r.readShort());
        c.setInvisible(r.readBoolean());
    }

    private void handleResuscitationSafeOff() throws NotEnoughDataException {
        // TODO ACT
    }

    private void handleResuscitationSafeOn() throws NotEnoughDataException {
        // TODO ACT
    }

    private void handleUpdateUsersOnline() throws NotEnoughDataException {
        System.out.println("Onlines: " + r.readShort());
    }

    private void handleGuildChat() throws NotEnoughDataException {
        // TODO ACT
        r.readString();
    }

    private void handleSafeModeOn() throws NotEnoughDataException {
        // TODO ACT
    }

    private void handleSafeModeOff() throws NotEnoughDataException {
        // TODO ACT
    }

}
